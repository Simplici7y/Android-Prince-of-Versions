package co.infinum.queenofversions;

public enum InAppUpdateError {

    API_NOT_AVAILABLE(),
    DOWNLOAD_NOT_PRESENT(),
    INSTALL_NOT_ALLOWED(),
    INSTALL_UNAVAILABLE(),
    INTERNAL_ERROR(),
    INVALID_REQUEST(),
    ERROR_UNKNOWN();

    InAppUpdateError() {
    }
}