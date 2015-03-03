/*using System;
using System.Collections.Generic;
using System.IO;
using System.Net;
using System.Net.Http;
using System.Net.Http.Headers;
using System.Runtime.Serialization;
using System.Text;
using System.Threading.Tasks;
using Hou.Common.Extensions;
using Newtonsoft.Json;

namespace Hou.Common.Translator
{
    public class BingTranslatorProvider : ITranslatorProvider
    {
        private BingAccessToken _token;
        private DateTime _tokenGetTime;
        public async Task<string> Translator(string word,string from="zh",string to="en")
        {
            if (_token==null||DateTime.Now>=_tokenGetTime.AddSeconds(_token.expires_in))
            {
                _token = await GetToken();
                _tokenGetTime = DateTime.Now;
            }
            string uri = "http://api.microsofttranslator.com/v2/Http.svc/Translate?text=" + HttpUtility.UrlEncode(word) + "&from=" + from + "&to=" + to;
          
            using (var client = new HttpClient())
            {
                client.DefaultRequestHeaders.Authorization = new AuthenticationHeaderValue("Bearer",_token.access_token);
               var message=await client.GetAsync(uri);
                var result=await message.Content.ReadAsStringAsync();
                return result;
            }
        }

        public async Task<BingAccessToken> GetToken()
        {
            var clientId = "TingDanCi";
            var clientSecret = "TingDanCi123TingDanCi123TingDanCi123";
            var tokenUrl = "https://datamarket.accesscontrol.windows.net/v2/OAuth2-13";

            using (var client = new HttpClient())
            {
                var keyValues = new List<KeyValuePair<string, string>>();
                keyValues.Add(new KeyValuePair<string, string>("grant_type", "client_credentials"));
                keyValues.Add(new KeyValuePair<string, string>("client_id", clientId));
                keyValues.Add(new KeyValuePair<string, string>("client_secret", clientSecret));
                keyValues.Add(new KeyValuePair<string, string>("scope", "http://api.microsofttranslator.com"));
                HttpContent content = new FormUrlEncodedContent(keyValues);
                var response = await client.PostAsync(tokenUrl, content);
                var result = await response.Content.ReadAsStringAsync();
                return JsonConvert.DeserializeObject<BingAccessToken>(result);
            }
          
        }
    }

    public class BingAccessToken
    { 
        public string access_token { get; set; } 
        public string token_type { get; set; } 
        public int expires_in { get; set; } 
        public string scope { get; set; }
    }
}*/