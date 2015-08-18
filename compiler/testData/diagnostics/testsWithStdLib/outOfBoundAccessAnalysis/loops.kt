fun forBreak() {
    val arr = IntArray(3)
    for (i in 1..5) {
        <!OUT_OF_BOUND_ACCESS!>arr[i]<!>          // obvious out-of-bound access
    }
    for (i in 1..5) {
        if (i > 2) {
            break
            <!UNREACHABLE_CODE!>arr[400]<!>
        }
        arr[i]                                    // no alarm here
    }
    for (i in 3..5) {
        if (i > 4) {
            break
            <!UNREACHABLE_CODE!>arr[400]<!>
        }
        <!OUT_OF_BOUND_ACCESS!>arr[i]<!>          // `i` can still be 3
    }
    for (i in 1..5) {
        if (i == 2) {
            break
            <!UNREACHABLE_CODE!>arr[400]<!>
        }
        <!OUT_OF_BOUND_ACCESS!>arr[i]<!>          // false alarm, `i` is thought to be 5. In general it's not easy to handle this situation
    }
}

fun forContinue() {
    val arr = IntArray(3)
    for (i in 1..5) {
        if (i > 2) {
            continue
            <!UNREACHABLE_CODE!>arr[400]<!>
        }
        arr[i]                                    // no alarm here
    }
    for (i in 3..5) {
        if (i > 4) {
            continue
            <!UNREACHABLE_CODE!>arr[400]<!>
        }
        <!OUT_OF_BOUND_ACCESS!>arr[i]<!>          // `i` can still be 3
    }
    for (i in 1..5) {
        if (i == 2) {
            continue
            <!UNREACHABLE_CODE!>arr[400]<!>
        }
        <!OUT_OF_BOUND_ACCESS!>arr[i]<!>          // false alarm, `i` is thought to be 5. In general it's not easy to handle this situation
    }
}

fun whileBreak(cond: Boolean) {
    val arr = IntArray(3)
    while (cond) {
        var i = 100
        <!OUT_OF_BOUND_ACCESS!>arr[i]<!>
    }
    while (cond) {
        var i = 100
        if (i > 2) {
            break
        }
        arr[i]
    }
}

fun whileContinue(cond: Boolean) {
    val arr = IntArray(3)
    while (cond) {
        var i = 100
        if (i > 2) {
            continue
        }
        arr[i]
    }
}