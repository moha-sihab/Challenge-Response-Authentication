using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace CramAuthApi.Models.Responses
{
    public class BaseResponse<T>
    {
        public bool Success { get; set; }
        public string Message { get; set; } = string.Empty;
        public T? Data { get; set; }
        public object? Errors { get; set; }

        public static BaseResponse<T> Ok(T data, string message = "Success")
            => new() { Success = true, Data = data, Message = message };

        public static BaseResponse<T> Fail(string message, object? errors = null)
            => new() { Success = false, Message = message, Errors = errors };
    }
}