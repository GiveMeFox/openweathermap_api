package cc.givemefox.result

/**
 * [Result] is a type that represents either success ([Ok]) or failure ([Err]).
 *
 * - Elm: [Result](http://package.elm-lang.org/packages/elm-lang/core/5.1.1/Result)
 * - Haskell: [Data.Either](https://hackage.haskell.org/package/base-4.10.0.0/docs/Data-Either.html)
 * - Rust: [Result](https://doc.rust-lang.org/std/result/enum.Result.html)
 */
sealed class Result<out V, out E> {

    abstract operator fun component1(): V?
    abstract operator fun component2(): E?

    companion object {

        /**
         * Invokes a [function] and wraps it in a [Result], returning an [Err]
         * if an [Exception] was thrown, otherwise [Ok].
         */
        @Deprecated("Use top-level runCatching instead", ReplaceWith("runCatching(function)"))
        inline fun <V> of(function: () -> V): Result<V, Exception> {
            return try {
                Ok(function.invoke())
            } catch (ex: Exception) {
                Err(ex)
            }
        }
    }
}

/**
 * Represents a successful [Result], containing a [value].
 */
class Ok<out V>(val value: V) : Result<V, Nothing>() {

    override fun component1(): V = value
    override fun component2(): Nothing? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as Ok<*>

        return value == other.value
    }

    override fun hashCode(): Int = value.hashCode()
    override fun toString(): String = "Ok($value)"
}

/**
 * Represents a failed [Result], containing an [error].
 */
class Err<out E>(val error: E) : Result<Nothing, E>() {

    override fun component1(): Nothing? = null
    override fun component2(): E = error

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as Err<*>

        return error == other.error
    }

    override fun hashCode(): Int = error.hashCode()
    override fun toString(): String = "Err($error)"
}
