using System;
using System.Collections.Generic;
using System.Linq;
using System.Security.Cryptography;
using System.Text;
using System.Threading.Tasks;
using CramAuthApi.Data;
using CramAuthApi.DTOs;
using CramAuthApi.Helpers.CramAuthApi.Helpers;
using CramAuthApi.Models;
using CramAuthApi.Models.Responses;
using Microsoft.AspNetCore.Mvc;

namespace CramAuthApi.Controllers
{
    [ApiController]
    [Route("api/[controller]")]
    public class ChallengeController : ControllerBase
    {
        private readonly CramDbContext _context;
        private readonly JwtTokenGenerator _jwt;

        public ChallengeController(CramDbContext context, JwtTokenGenerator jwt)
        {
            _context = context;
            _jwt = jwt;
        }

        [HttpPost]
        public IActionResult CreateChallenge([FromQuery] int userId)
        {
            var user = _context.Users.Find(userId);
            if (user == null)
            {
                return NotFound(BaseResponse<string>.Fail("User not found"));
            }

            var bytes = new byte[8];
            RandomNumberGenerator.Fill(bytes);
            var challengeText = BitConverter.ToString(bytes).Replace("-", "");

            var now = DateTime.UtcNow;
            var challenge = new Challenge
            {
                UserId = userId,
                ChallengeText = challengeText,
                IssuedAt = now,
                ExpiresAt = now.AddMinutes(10),
                IsUsed = false
            };
            _context.Challenges.Add(challenge);
            _context.SaveChanges();

            return Ok(BaseResponse<object>.Ok(new
            {
                challenge.Id,
                challenge.ChallengeText,
                challenge.IssuedAt
            }, "Challenge create successfully"));
        }

        [HttpPost("respond")]
        public IActionResult VerifyChallenge([FromBody] ChallengeResponseDto dto)
        {
            var user = _context.Users.Find(dto.UserId);
            if (user == null)
            {
                return NotFound(BaseResponse<string>.Fail("User not found"));
            }

            var challenge = _context.Challenges.FirstOrDefault(x =>
                x.Id == dto.ChallengeId &&
                x.UserId == dto.UserId &&
                !x.IsUsed);
            if (challenge == null)
            {
                return NotFound(BaseResponse<string>.Fail("Challenge not found or already used"));
            }

            if (DateTime.UtcNow > challenge.ExpiresAt)
            {
                return Unauthorized(BaseResponse<string>.Fail("Challenge expired"));
            }

            var publicKeyBytes = Convert.FromBase64String(user.PublicKey);
            using var ecdsa = ECDsa.Create();
            ecdsa.ImportSubjectPublicKeyInfo(publicKeyBytes, out _);

            var challengeBytes = Encoding.UTF8.GetBytes(challenge.ChallengeText);
            var signatureBytes = Convert.FromBase64String(dto.Signature);

            var isValid = ecdsa.VerifyData(challengeBytes, signatureBytes, HashAlgorithmName.SHA256);

            if (!isValid)
            {
                return Unauthorized(BaseResponse<string>.Fail("Invalid signature"));
            }

            challenge.IsUsed = true;
            _context.SaveChanges();

            var token = _jwt.GenerateToken(user.Id, user.Username);

            return Ok(BaseResponse<object>.Ok(new LoginDto
            {
                Token = token,
                Id = user.Id,
                Username = user.Username
            }, "Challenge verified successfully"));


        }
    }
}