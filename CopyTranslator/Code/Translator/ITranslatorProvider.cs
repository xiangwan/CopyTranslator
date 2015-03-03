using System.Collections.Generic;
using System.Threading.Tasks;

namespace CopyTranslator.Code.Translator
{
    public interface ITranslatorProvider
    {
        Task<Dictionary<string, string>> Translator(string word, string from = "en", string to = "zh");
    }
}