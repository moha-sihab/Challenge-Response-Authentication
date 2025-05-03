using Microsoft.EntityFrameworkCore;
using CramAuthApi.Models;

namespace CramAuthApi.Data
{
    public class CramDbContext : DbContext
    {
        public CramDbContext(DbContextOptions<CramDbContext> options)
            : base(options)
        {
        }

        public DbSet<User> Users => Set<User>();
        public DbSet<Challenge> Challenges => Set<Challenge>();
    }
}
