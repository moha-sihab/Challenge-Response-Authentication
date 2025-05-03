using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace CramAuthApi.Models
{
    public class User
    {
        public int Id { get; set; }
        public string Username { get; set; } =  string.Empty;
        public string Salt { get; set; } = string.Empty;
        public string PasswordHash  { get; set; } = string.Empty;

        public ICollection<Challenge>? Challenges{ get; set; } 


    }
}