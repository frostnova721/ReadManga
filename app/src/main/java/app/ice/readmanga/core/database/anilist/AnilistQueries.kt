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
              native
              userPreferred
            }
            bannerImage
            synonyms
            coverImage {
              large
              medium
            }
            genres
            description
            source
            type
            episodes
            status
            nextAiringEpisode {
              episode
              airingAt
              timeUntilAiring
            }
            tags {
              name
              category
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
            studios {
              edges {
                isMain
                node {
                  isAnimationStudio
                  name
                  id
                }
              }
            }
            duration
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
                    medium
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
              native
              userPreferred
            }
            coverImage {
              extraLarge
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
              native
            }
            coverImage {
              extraLarge
              large
            }
          }
        }
      }
    }
  }
}"""
    }
}