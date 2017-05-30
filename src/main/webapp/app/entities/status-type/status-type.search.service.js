(function() {
    'use strict';

    angular
        .module('myappApp')
        .factory('StatusTypeSearch', StatusTypeSearch);

    StatusTypeSearch.$inject = ['$resource'];

    function StatusTypeSearch($resource) {
        var resourceUrl =  'api/_search/status-types/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
