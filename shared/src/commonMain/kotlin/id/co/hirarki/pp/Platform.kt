package id.co.hirarki.pp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform