var allTestFiles = [];
var TEST_REGEXP = /spec\.js$/i;

// Get a list of all the test files to include
Object.keys(window.__karma__.files).forEach(function (file) {
    if (TEST_REGEXP.test(file)) {
        // Normalize paths to RequireJS module names.
        // If you require sub-dependencies of test files to be loaded as-is (requiring file extension)
        // then do not normalize the paths
        var normalizedTestModule = file.replace(/^\/base\/|\.js$/g, '');
        allTestFiles.push(normalizedTestModule);
    }
});
require.config({
    // Karma serves files under /base, which is the basePath from your config file
    baseUrl: '/base',

    // dynamically load all test files
    deps: allTestFiles,

    // we have to kickoff jasmine, as it is asynchronous
    callback: window.__karma__.start,

    paths: {
        handlebars: 'node_modules/handlebars/dist/handlebars',
        i18next: 'node_modules/i18next/i18next'
    },
    shim:
    {
        "handlebars.runtime":
        {
            "exports": "Handlebars"
        }
    }
});
