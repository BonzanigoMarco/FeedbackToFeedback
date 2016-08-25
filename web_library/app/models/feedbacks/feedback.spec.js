define(["require", "exports", './feedback', '../configurations/configuration_factory'], function (require, exports, feedback_1, configuration_factory_1) {
    "use strict";
    describe('Feedback', function () {
        it('should validate itself according to the given configuration data', function () {
            var configurationData = {
                "id": 1,
                "type": "PUSH",
                "general_configurations": null,
                "mechanisms": [
                    {
                        "id": 1,
                        "type": "TEXT_TYPE",
                        "active": true,
                        "order": 1,
                        "canBeActivated": false,
                        "parameters": [
                            {
                                "key": "maxLength",
                                "value": 100
                            },
                            {
                                "key": "title",
                                "value": "Feedback"
                            },
                            {
                                "key": "hint",
                                "value": "Enter your feedback"
                            }
                        ]
                    }
                ]
            };
            var configuration = configuration_factory_1.ConfigurationFactory.createByData(configurationData);
            var feedback = new feedback_1.Feedback('Feedback', 'application', null, 'This is my feedback!', 1.0, []);
            expect(feedback.validate(configuration)).toBeTruthy();
        });
        it('should return error messages if the validation was not successful', function () {
            var configurationData = {
                "id": 1,
                "type": "PUSH",
                "general_configurations": null,
                "mechanisms": [
                    {
                        "id": 1,
                        "type": "TEXT_TYPE",
                        "active": true,
                        "order": 1,
                        "canBeActivated": false,
                        "parameters": [
                            {
                                "key": "maxLength",
                                "value": 100
                            },
                            {
                                "key": "title",
                                "value": "Feedback"
                            },
                            {
                                "key": "hint",
                                "value": "Enter your feedback"
                            }
                        ]
                    }
                ]
            };
            var configuration = configuration_factory_1.ConfigurationFactory.createByData(configurationData);
            var feedback = new feedback_1.Feedback('Feedback', 'application', null, '', 1.0, []);
            var errors = feedback.validate(configuration);
            expect(errors.textMechanism.length).toBe(1);
            expect(errors.ratingMechanism.length).toBe(0);
            expect(errors.general.length).toBe(0);
            expect(errors.textMechanism[0]).toEqual('Please input a text');
        });
    });
});
//# sourceMappingURL=feedback.spec.js.map