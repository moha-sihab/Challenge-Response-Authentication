using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace CramAuthApi.Models
{
    public class Challenge
    {
        public int Id { get; set; }
        public int UserId { get; set; }
        public string ChallengeText { get; set; } = string.Empty;
        public DateTime IssuedAt { get; set; } = DateTime.UtcNow;

        public DateTime ExpiresAt { get; set; } = DateTime.UtcNow;
        public bool IsUsed { get; set; } = false;

        public User? User { get; set; }
    }
}