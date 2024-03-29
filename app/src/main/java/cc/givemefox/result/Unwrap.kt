package cc.givemefox.result

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

class UnwrapException(message: String) : Exception(message)

/**
 * Unwraps a [Result], yielding the [value][Ok.value].
 *
 * - Rust: [Result.unwrap](https://doc.rust-lang.org/std/result/enum.Result.html#method.unwrap)
 *
 * @throws UnwrapException if the [Result] is an [Err], with a message containing the [error][Err.error].
 */
@OptIn(ExperimentalContracts::class)
fun <V, E> Result<V, E>.unwrap(): V {
    contract {
        returns() implies (this@unwrap is Ok<V>)
    }

    return when (this) {
        is Ok -> value
        is Err -> throw UnwrapException("called Result.unwrap on an Err value $error")
    }
}

@OptIn(ExperimentalContracts::class)
@Deprecated("Use lazy-evaluating variant instead", ReplaceWith("expect { message }"))
infix fun <V, E> Result<V, E>.expect(message: String): V {
    contract {
        returns() implies (this@expect is Ok<V>)
    }

    return expect { message }
}

/**
 * Unwraps a [Result], yielding the [value][Ok.value].
 *
 * - Rust: [Result.expect](https://doc.rust-lang.org/std/result/enum.Result.html#method.expect)
 *
 * @param message The message to include in the [UnwrapException] if the [Result] is an [Err].
 * @throws UnwrapException if the [Result] is an [Err], with the specified [message].
 */
@OptIn(ExperimentalContracts::class)
inline infix fun <V, E> Result<V, E>.expect(message: () -> Any): V {
    contract {
        callsInPlace(message, InvocationKind.AT_MOST_ONCE)
        returns() implies (this@expect is Ok<V>)
    }

    return when (this) {
        is Ok -> value
        is Err -> throw UnwrapException("${message()} $error")
    }
}

/**
 * Unwraps a [Result], yielding the [error][Err.error].
 *
 * - Rust: [Result.unwrap_err](https://doc.rust-lang.org/std/result/enum.Result.html#method.unwrap_err)
 *
 * @throws UnwrapException if the [Result] is [Ok], with a message containing the [value][Ok.value].
 */
@OptIn(ExperimentalContracts::class)
fun <V, E> Result<V, E>.unwrapError(): E {
    contract {
        returns() implies (this@unwrapError is Err<E>)
    }

    return when (this) {
        is Ok -> throw UnwrapException("called Result.unwrapError on an Ok value $value")
        is Err -> error
    }
}

@OptIn(ExperimentalContracts::class)
@Deprecated("Use lazy-evaluating variant instead", ReplaceWith("expectError { message }"))
infix fun <V, E> Result<V, E>.expectError(message: String): E {
    contract {
        returns() implies (this@expectError is Err<E>)
    }

    return expectError { message }
}

/**
 * Unwraps a [Result], yielding the [error][Err.error].
 *
 * - Rust: [Result.expect_err](https://doc.rust-lang.org/std/result/enum.Result.html#method.expect_err)
 *
 * @param message The message to include in the [UnwrapException] if the [Result] is [Ok].
 * @throws UnwrapException if the [Result] is [Ok], with the specified [message].
 */
@OptIn(ExperimentalContracts::class)
inline infix fun <V, E> Result<V, E>.expectError(message: () -> Any): E {
    contract {
        callsInPlace(message, InvocationKind.AT_MOST_ONCE)
        returns() implies (this@expectError is Err<E>)
    }

    return when (this) {
        is Ok -> throw UnwrapException("${message()} $value")
        is Err -> error
    }
}