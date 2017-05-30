(function() {
    'use strict';

    angular
        .module('myappApp')
        .factory('TermSearch', TermSearch);

    TermSearch.$inject = ['$resource'];

    function TermSearch($resource) {
        var resourceUrl =  'api/_search/terms/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
