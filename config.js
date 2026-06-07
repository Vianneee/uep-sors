// Global Configuration for UEP SORS
const SORS_CONFIG = (() => {
    const isLocal = ['localhost', '127.0.0.1'].includes(window.location.hostname);
    const API_ORIGIN = isLocal ? 'http://localhost:8080' : window.location.origin;

    return {
        API_BASE: `${API_ORIGIN}/api`,
        ORGS_API: `${API_ORIGIN}/api/organizations`
    };
})();
