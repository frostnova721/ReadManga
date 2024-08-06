package app.ice.readmanga.core.database.anilist

class AnilistQueries {
    val searchQuery: (query: String) -> String = { query ->
        """query {
                Page(perPage: 25) {
                    media(search: "$query", type: MANGA, isAdult: false) {
                        id
                        idMal
                        title {
                            english
                            romaji
                        }
                        averageScore
                        coverImage {
                            large
                        }
                    }
                }
            }"""
    }

    // mediaListEntry {
    //        status
    //      }
    val infoQuery: (anilistId: Int) -> String = { id ->
        """  {
        Page(perPage: 100) {
          media(id: $id) {
            title {
              romaji
              english
            }
            bannerImage
            synonyms
            coverImage {
              large
            }
            genres
            description
            source
            type
            chapters
            status
            nextAiringEpisode {
              episode
              airingAt
              timeUntilAiring
            }
            tags {
              name
            }
            startDate {
              year
              month
              day
            },
            endDate {
              year
              month
              day
            },
            averageScore
            popularity
            characters {
              edges {
                node {
                  name {
                    full
                    native
                  }
                  image {
                    large
                  }
                }
                role
              }
            }
            recommendations {
        nodes {
          mediaRecommendation {
            id
            type
            averageScore
            title {
              romaji
              english
            }
            coverImage {
              large
            }
            
          }
        }
      }
      relations {
        edges {
          relationType
          node {
            id
            type
            title {
              romaji
              english
            }
            averageScore
            coverImage {
              large
            }
          }
        }
      }
    }
  }
}"""
    }

    val trendingQuery = """{
  Page(perPage: 25) {
    media(type: MANGA, sort: TRENDING_DESC, isAdult: false) {
      id
      title {
        english
        romaji
      }
      genres
      averageScore
      bannerImage
      coverImage {
        large
      }
    }
  }
}
"""
}