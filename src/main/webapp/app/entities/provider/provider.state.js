(function() {
    'use strict';

    angular
        .module('myappApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('provider', {
            parent: 'entity',
            url: '/provider?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'myappApp.provider.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/provider/providers.html',
                    controller: 'ProviderController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('provider');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('provider-detail', {
            parent: 'provider',
            url: '/provider/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'myappApp.provider.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/provider/provider-detail.html',
                    controller: 'ProviderDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('provider');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Provider', function($stateParams, Provider) {
                    return Provider.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'provider',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('provider-detail.edit', {
            parent: 'provider-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/provider/provider-dialog.html',
                    controller: 'ProviderDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Provider', function(Provider) {
                            return Provider.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('provider.new', {
            parent: 'provider',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/provider/provider-dialog.html',
                    controller: 'ProviderDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                logoFilepath: null,
                                summary: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('provider', null, { reload: 'provider' });
                }, function() {
                    $state.go('provider');
                });
            }]
        })
        .state('provider.edit', {
            parent: 'provider',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/provider/provider-dialog.html',
                    controller: 'ProviderDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Provider', function(Provider) {
                            return Provider.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('provider', null, { reload: 'provider' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('provider.delete', {
            parent: 'provider',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/provider/provider-delete-dialog.html',
                    controller: 'ProviderDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Provider', function(Provider) {
                            return Provider.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('provider', null, { reload: 'provider' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
