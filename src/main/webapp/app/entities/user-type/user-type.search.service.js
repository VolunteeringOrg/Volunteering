(function() {
    'use strict';

    angular
        .module('myappApp')
        .factory('UserTypeSearch', UserTypeSearch);

    UserTypeSearch.$inject = ['$resource'];

    function UserTypeSearch($resource) {
        var resourceUrl =  'api/_search/user-types/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
