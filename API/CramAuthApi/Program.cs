using CramAuthApi.Data;
using Microsoft.EntityFrameworkCore;
using Microsoft.AspNetCore.Authentication.JwtBearer;
using Microsoft.IdentityModel.Tokens;
using System.Text;
using CramAuthApi.Helpers.CramAuthApi.Helpers;

var builder = WebApplication.CreateBuilder(args);
var connectionString = builder.Configuration.GetConnectionString("DefaultConnection");
var jwtSettings = builder.Configuration.GetSection("JwtSettings");

builder.Services.AddDbContext<CramDbContext>(options =>
    options.UseSqlServer(connectionString));



builder.Services.AddAuthentication(JwtBearerDefaults.AuthenticationScheme)
    .AddJwtBearer(options =>
    {
        options.TokenValidationParameters = new TokenValidationParameters
        {
            ValidateIssuer = true,
            ValidateAudience = true,
            ValidateIssuerSigningKey = true,
            ValidIssuer = jwtSettings["Issuer"],
            ValidAudience = jwtSettings["Audience"],
            IssuerSigningKey = new SymmetricSecurityKey(
                Encoding.UTF8.GetBytes(jwtSettings["Key"]!))
        };
    });


builder.Services.AddAuthorization();
builder.Services.AddControllers();
builder.Services.AddEndpointsApiExplorer();
builder.Services.AddSwaggerGen();

builder.Services.AddScoped<JwtTokenGenerator>();
//builder.WebHost.UseUrls("http://0.0.0.0:5113", "https://0.0.0.0:7242");
//builder.WebHost.UseUrls("https://localhost:7242", "http://localhost:5113");

builder.WebHost.ConfigureKestrel(options =>
{
    options.ListenAnyIP(7242, listenOptions =>
    {
       // listenOptions.UseHttps("/Users/moha-sihab/Documents/Cert/192.168.1.7.pem", "/Users/moha-sihab/Documents/Cert/192.168.1.7-key.pem");
       listenOptions.UseHttps("/Users/moha-sihab/Documents/Cert/192.168.1.7.pfx", "Kulminasi");
    });

    options.ListenAnyIP(5112); // Optional: plain HTTP fallback
});


builder.Services.AddCors(options =>
{
    options.AddPolicy("AllowAll", policy =>
    {
        policy.AllowAnyOrigin()
              .AllowAnyMethod()
              .AllowAnyHeader();
    });
});


var app = builder.Build();

// Configure the HTTP request pipeline.
if (app.Environment.IsDevelopment())
{
    app.UseSwagger();
    app.UseSwaggerUI();
}

app.UseCors("AllowAll");

app.UseHttpsRedirection();
app.UseAuthentication(); 
app.UseAuthorization();


app.MapControllers();
app.Run();
