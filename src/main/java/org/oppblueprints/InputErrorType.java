package org.oppblueprints;

public enum InputErrorType {
    None,
    CommandSyntax,
    UnknownCommand,
    RowIndexUndefined,
    ColIndexUndefined,
    UnknownChar,
    ColIndexTooLarge,
    RowIndexTooLarge
}
