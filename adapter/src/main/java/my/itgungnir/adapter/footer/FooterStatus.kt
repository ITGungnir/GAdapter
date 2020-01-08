package my.itgungnir.adapter.footer

data class FooterStatus(val status: Status) {

    enum class Status(val flag: Int) {
        IDLE(0),
        PROGRESSING(1),
        NO_MORE(2),
        FAILED(3)
    }
}