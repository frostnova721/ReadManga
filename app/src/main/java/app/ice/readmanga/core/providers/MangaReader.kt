package app.ice.readmanga.core.providers

import android.content.Context
import android.widget.Toast
import app.ice.readmanga.types.Chapters
import app.ice.readmanga.types.ChaptersResult
import app.ice.readmanga.types.SearchResult
import app.ice.readmanga.utils.get
import org.json.JSONObject
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class MangaReader(context: Context): Provider() {
    private val context = context
    private val baseUrl = "https://mangareader.to";

    override suspend fun search(query: String): List<SearchResult> {
        val searchUrl = "$baseUrl/search?keyword=$query";

        val html = get(searchUrl);

        val document: Document = Jsoup.parse(html)
        val elements = document.select(".item.item-spc")

        val searchRes = mutableListOf<SearchResult>()

        for(ele in elements) {
            val cover = ele.select("a.manga-poster img").attr("src")
            val id = ele.select("a.manga-poster").attr("href").replace("/", "")
            val title = ele.select("h3.manga-name a").text()

            searchRes.add(SearchResult(id, title, cover))
        }

        return searchRes;
    }

    override suspend fun getChapters(id: String): List<ChaptersResult> {
        val url = "$baseUrl/$id"
        val res = get(url)
        val doc = Jsoup.parse(res)

        val chapterResult = mutableListOf<ChaptersResult>()

        val elements = doc.select(".chapters-list-ul").first()?.children() ?: throw Error("NO ELEMENT")

        for(e in elements) {
            val lang = e.attr("id").split("-")[0]
            val langSpecificChapters = mutableListOf<Chapters>()

            val subElements = e.children()

            for(se in subElements) {
                val chapter = se.attr("data-number")
                val idPart = se.selectFirst("a")?.attr("href") ?: continue
                val link = (baseUrl + idPart)
                langSpecificChapters.add(Chapters(chapter, link))
            }
            if(langSpecificChapters.isEmpty()) continue;
            chapterResult.add(ChaptersResult(lang, langSpecificChapters))
        }

        return chapterResult;
    }

    override suspend fun getPages(chapterLink: String, quality: String): List<String> {
        val res = get(chapterLink)
        var doc = Jsoup.parse(res)

        val readingId = doc.select("div#wrapper").attr("data-reading-id")

        if(readingId.isEmpty()) throw Exception("Couldn't find the reading ID");

        val ajaxLink = "https://mangareader.to/ajax/image/list/chap/$readingId?mode=vertical&quality=$quality&hozPageSize=1";

        val apiRes = get(ajaxLink)
        val jsonObject = JSONObject(apiRes)
        doc = Jsoup.parse(jsonObject.getString("html"))
        val pages = mutableListOf<String>()
        val elements = doc.select("div.iv-card")
        for(e in elements) {
            if(e.classNames().size == 1 && e.hasClass("iv-card")) {
                pages.add(e.attr("data-url").replace("&amp;", "&"))
            } else {
                Toast.makeText(context, "Shuffled images. Unreadable!", Toast.LENGTH_SHORT).show()
                return emptyList();
            }
        }
        return pages;
    }
}