using System;
using System.Collections.Generic;
using System.Net.Http;
using System.Threading.Tasks;
using Newtonsoft.Json;

namespace CopyTranslator.Code.Translator
{
    public class BaiduTranslatorProvider : ITranslatorProvider
    {
        public BaiduTranslatorProvider(string clientId = "pAPMGEOIRyWRACSMKbdSMS8s")
        {
            ClinetId = clientId;
        }

        private string _clientId;

        public string ClinetId
        {
            get
            {
                if (string.IsNullOrEmpty(_clientId))
                {
                    _clientId ="pAPMGEOIRyWRACSMKbdSMS8s";
                }
                return _clientId;
            }
            set
            { 
                _clientId = value;
            }
        }
       
        public async Task<Dictionary<string, string>> Translator(string word, string @from = "en", string to = "zh")
        {
            var result = new Dictionary<string, string>();
            try
            {
                string urlBase = "http://openapi.baidu.com/public/2.0/bmt/translate?client_id={3}&q={0}&from={1}&to={2}";
                var uri = string.Format(urlBase, word,@from, to, _clientId);
                using (var client = new HttpClient())
                {
                    var message = await client.GetAsync(uri);
                    var raw = await message.Content.ReadAsStringAsync();
                    var obj = JsonConvert.DeserializeObject<BaiduTranslatorResult>(raw);
                    if (string.IsNullOrEmpty(obj.ErrorCode))
                    {
                        if (obj.TransResult != null)
                        {
                            foreach (var element in obj.TransResult)
                            {
                                if (!result.ContainsKey(element.Src))
                                {
                                    result.Add(element.Src, element.Dst);
                                }
                            }
                        }
                    }
                    else
                    {
                        result.Add("error_code", obj.ErrorCode);
                        result.Add("error_msg", obj.ErrorMsg);
                    }
                }
            }
            catch (Exception e)
            {
                result.Add("error_code", "500");
                result.Add("error_msg", e.Message);
            }
            return result;
        }
         
    }

    public class BaiduTranslatorResultElement
    {

        [JsonProperty("src")]
        public string Src { get; set; }

        [JsonProperty("dst")]
        public string Dst { get; set; }
    }

    public class BaiduTranslatorResult
    {
        [JsonProperty("error_code")]
        public string ErrorCode { get; set; }

        [JsonProperty("error_msg")]
        public string ErrorMsg { get; set; }

        [JsonProperty("query")]
        public string Query { get; set; }

        [JsonProperty("from")]
        public string From { get; set; }

        [JsonProperty("to")]
        public string To { get; set; }

        [JsonProperty("trans_result")]
        public BaiduTranslatorResultElement[] TransResult { get; set; }
    }
}