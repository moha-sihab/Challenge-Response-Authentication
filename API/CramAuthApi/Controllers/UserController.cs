using Microsoft.AspNetCore.Mvc;
using CramAuthApi.Data;
using CramAuthApi.Models;
using CramAuthApi.DTOs;
using CramAuthApi.Helpers;
using CramAuthApi.Models.Responses;
using CramAuthApi.Helpers.CramAuthApi.Helpers;

namespace CramAuthApi.Controllers
{
    [ApiController]
    [Route("api/[controller]")]
    public class UsersController : ControllerBase
    {
        private readonly CramDbContext _context;
        private readonly JwtTokenGenerator _jwt;

        public UsersController(CramDbContext context, JwtTokenGenerator jwt)
        {
            _context = context;
            _jwt = jwt;

        }

        [HttpGet]
        public IActionResult GetUsers()
        {
            var users = _context.Users.Select(u => new UserDto
            {
                Id = u.Id,
                Username = u.Username,
            }).ToList();

            return Ok(BaseResponse<List<UserDto>>.Ok(users));
        }

        [HttpPost("register")]
        public IActionResult Register(RegisterUserDto dto)
        {
            try
            {
                var existing = _context.Users.FirstOrDefault(u => u.Username == dto.Username);
                if (existing != null)
                {
                    return BadRequest(BaseResponse<string>.Fail("Username already exists"));

                }
                var salt = Guid.NewGuid().ToString();
                var hashed = PasswordHasher.HashPassword(dto.Password, salt);
                var user = new User
                {
                    Username = dto.Username,
                    Salt = salt,
                    PasswordHash = hashed
                };

                _context.Users.Add(user);
                _context.SaveChanges();

                return Ok(BaseResponse<object>.Ok(new
                {
                    user.Username,
                }, "User Created Successfully"));
            }
            catch (Exception ex)
            {
                return StatusCode(500, BaseResponse<string>.Fail("An error occured", new
                {
                    exception = ex.Message,
                }));
            }

        }

        [HttpPost("login")]
        public IActionResult Login(LoginRequestDto dto)
        {
            try
            {
                var user = _context.Users.FirstOrDefault(u => u.Username == dto.Username);
                if (user == null)
                {
                    return NotFound(BaseResponse<string>.Fail("User not found"));
                }

                var inputHash = PasswordHasher.HashPassword(dto.Password, user.Salt);

                if (inputHash != user.PasswordHash)
                {
                    return Unauthorized(BaseResponse<string>.Fail("Invalid password"));
                }

                var token = _jwt.GenerateToken(user.Id, user.Username);

                return Ok(BaseResponse<object>.Ok(new
                {
                    token
                }, "Login successfull"));
            }
            catch (Exception ex)
            {
                return StatusCode(500, BaseResponse<string>.Fail("An error occured", new
                {
                    exception = ex.Message,
                }));
            }
        }
    }
}
