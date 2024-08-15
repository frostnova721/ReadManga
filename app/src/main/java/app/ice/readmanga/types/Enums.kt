package app.ice.readmanga.types

enum class Countries(val countryCode: String) {
    JAPAN("JP"),
    CHINA("CN"),
    KOREA("KR"),
    ANY("ANY")
}

enum class MangaTypes(val country: Countries) {
    MANGA(Countries.JAPAN),
    MANHUA(Countries.CHINA),
    MANHWA(Countries.KOREA),
    NOVEL(Countries.ANY),
}
