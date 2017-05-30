(function() {
    'use strict';

    angular
        .module('myappApp')
        .factory('ProgramSearch', ProgramSearch);

    ProgramSearch.$inject = ['$resource'];

    function ProgramSearch($resource) {
        var resourceUrl =  'api/_search/programs/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
