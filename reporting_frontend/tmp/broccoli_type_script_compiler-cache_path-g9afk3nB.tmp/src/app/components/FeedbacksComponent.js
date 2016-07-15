"use strict";
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};
/**
 * Created by flo on 14.07.16.
 */
var core_1 = require('@angular/core');
var feedback_service_1 = require('../services/feedback.service');
var button_1 = require('@angular2-material/button');
var input_1 = require('@angular2-material/input/input');
var FeedbacksComponent = (function () {
    function FeedbacksComponent(feedbackService) {
        this.feedbackService = feedbackService;
    }
    FeedbacksComponent.prototype.getFeedbacks = function (appname) {
        var _this = this;
        this.application = appname;
        if (this.application != "") {
            this.feedbackService
                .GetFeedbacks(this.application)
                .subscribe(function (data) { return _this.feedbacks = data; }, function (error) { return console.log(error); });
        }
    };
    FeedbacksComponent = __decorate([
        core_1.Component({
            selector: "feedbacks",
            template: "\n<h2>Feedbacks</h2>\n<div>\n  <div style=\"float: left\">\n    <md-input placeholder=\"enter application name\" #appname maxlength=\"100\" class=\"demo-full-width\">\n    </md-input>\n  </div>\n  <button style=\"margin-left: 10px\" md-raised-button (click) = \"getFeedbacks(appname.value)\">\n  load\n  </button>\n</div>\n<ul>\n    <li *ngFor=\"#feedback of feedbacks\">\n        Title: {{feedback.title}}\n        Text: {{feedback.text}}\n    </li>\n</ul>\n",
            providers: [feedback_service_1.FeedbackService],
            directives: [button_1.MdButton, input_1.MD_INPUT_DIRECTIVES]
        }), 
        __metadata('design:paramtypes', [feedback_service_1.FeedbackService])
    ], FeedbacksComponent);
    return FeedbacksComponent;
}());
exports.FeedbacksComponent = FeedbacksComponent;
//# sourceMappingURL=FeedbacksComponent.js.map