using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace CramAuthApi.DTOs
{
    public class ChallengeResponseDto
    {
        public int UserId { get; set; }
        public int ChallengeId { get; set; }
        public string Signature { get; set; } = string.Empty;

    }
}