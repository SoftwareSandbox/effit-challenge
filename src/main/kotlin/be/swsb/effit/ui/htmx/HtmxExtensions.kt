package be.swsb.effit.ui.htmx

import kotlinx.html.HTMLTag

sealed class HxTarget(val value: String) {
    data object `this` : HxTarget("this")
    data object Body : HxTarget("body")
    class Target(value: String): HxTarget(value)
}

fun HTMLTag.hxTarget(target: HxTarget) {
    attributes += "hx-target" to target.value
}

fun HTMLTag.hxPost(url: String) {
    attributes += "hx-post" to url
}
fun HTMLTag.hxGet(url: String) {
    attributes += "hx-get" to url
}
fun HTMLTag.hxIndicator(`class`: String) {
    attributes += "hx-indicator" to ".$`class`"
}

sealed class HxTriggerModifier(val value: String) {
    data object Changed : HxTriggerModifier("changed")
    class Delay(timeInterval: String) : HxTriggerModifier("delay:$timeInterval")
    class Throttle(timeInterval: String) : HxTriggerModifier("throttle:$timeInterval")
    class From(cssSelector: String) : HxTriggerModifier("from:$cssSelector")
}

fun HTMLTag.hxTrigger(event: String, vararg modifiers: HxTriggerModifier) {
    attributes += "hx-trigger" to "$event ${modifiers.joinToString(" ") { it.value }}"
}

sealed class HxSwap(val value: String) {
    /** the default, puts the content inside the target element **/
    data object InnerHTML : HxSwap("innerHTML")

    /** replaces the entire target element with the returned content **/
    data object OuterHTML : HxSwap("outerHTML")

    /** prepends the content before the first child inside the target **/
    data object AfterBegin : HxSwap("afterbegin")

    /** prepends the content before the target in the targets parent element **/
    data object BeforeBegin : HxSwap("beforebegin")

    /** appends the content after the last child inside the target **/
    data object BeforeEnd : HxSwap("beforeend")

    /** appends the content after the target in the targets parent element **/
    data object AfterEnd : HxSwap("afterend")

    /** deletes the target element regardless of the response **/
    data object Delete : HxSwap("delete")

    /** does not append content from response (Out of Band Swaps and Response Headers will still be processed) **/
    data object None : HxSwap("none")

    /** Using the multiswap extension **/
    class Multi(vararg ids: String) : HxSwap("multi:${ids.joinToString(",") { id -> "#$id:outerHTML" }}")

}

fun HTMLTag.hxSwap(hxSwap: HxSwap) {
    attributes += "hx-swap" to hxSwap.value
}

fun HTMLTag.hxBoost(value: Boolean = true) {
    attributes += "hx-boost" to "$value"
}

const val HTMX_INDICATOR = "htmx-indicator"