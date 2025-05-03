using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace CramAuthApi.Helpers
{
    using Microsoft.Extensions.Configuration;
    using Microsoft.IdentityModel.Tokens;
    using System;
    using System.IdentityModel.Tokens.Jwt;
    using System.Security.Claims;
    using System.Text;

    namespace CramAuthApi.Helpers
    {
        public class JwtTokenGenerator
        {
            private readonly IConfiguration _config;

            public JwtTokenGenerator(IConfiguration config)
            {
                _config = config;
            }

            public string GenerateToken(int userId, string username)
            {
                var jwtSettings = _config.GetSection("JwtSettings");

                var key = new SymmetricSecurityKey(Encoding.UTF8.GetBytes(jwtSettings["Key"]!));

                var claims = new[]
                {
                new Claim(ClaimTypes.NameIdentifier, userId.ToString()),
                new Claim(ClaimTypes.Name, username)
            };

                var creds = new SigningCredentials(key, SecurityAlgorithms.HmacSha256);

                var token = new JwtSecurityToken(
                    issuer: jwtSettings["Issuer"],
                    audience: jwtSettings["Audience"],
                    claims: claims,
                    expires: DateTime.UtcNow.AddMinutes(int.Parse(jwtSettings["ExpiresInMinutes"]!)),
                    signingCredentials: creds
                );

                return new JwtSecurityTokenHandler().WriteToken(token);
            }
        }
    }

}