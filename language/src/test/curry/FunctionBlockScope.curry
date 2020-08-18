{
    name = "Daddy Cool";
    other = chuckme();
    name + " and " + other;
}

let chuckme {
    first = "Chuck";
    last = "Norris";
    name = first + " " + last;
    return name;
}