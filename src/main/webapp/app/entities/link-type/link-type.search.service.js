(function() {
    'use strict';

    angular
        .module('myappApp')
        .factory('LinkTypeSearch', LinkTypeSearch);

    LinkTypeSearch.$inject = ['$resource'];

    function LinkTypeSearch($resource) {
        var resourceUrl =  'api/_search/link-types/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
