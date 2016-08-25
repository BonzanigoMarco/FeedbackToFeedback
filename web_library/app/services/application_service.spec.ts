import {MockBackend} from './backends/mock_backend';
import {readJSON} from './mocks/mocks_loader';
import {ApplicationService} from './application_service';


describe('Application Service', () => {
    let applicationService:ApplicationService;

    beforeEach(() => {
        var applicationMockData = readJSON('app/services/mocks/applications_mock.json', '/base/');
        var backend = new MockBackend(applicationMockData);
        applicationService = new ApplicationService(backend);
    });

    it('should retrieve the application from a backend', () => {
        applicationService.retrieveApplication(1, function(application) {
            expect(application.name).toEqual('Senercon Website');
            expect(application.configurations.length).toBe(3);
        });
    });
});
