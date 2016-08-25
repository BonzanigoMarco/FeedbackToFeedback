import {Parameter} from './parameters/parameter';
import {Mechanism} from './mechanism';
import {ParameterValuePropertyPair} from '../parameters/parameter_value_property_pair';
import {ParameterInterface} from '../parameters/parameter_interface';


export class TextMechanism extends Mechanism {

    constructor(id:number, type:string, active:boolean, order?:number, canBeActivated?:boolean, parameters?:ParameterInterface[]) {
        super(id, type, active, order, canBeActivated, parameters);
    }

    getContext():any {
        var textareaStyle = this.getCssStyle(this, [new ParameterValuePropertyPair('textareaFontColor', 'color')]);
        var labelStyle = this.getCssStyle(this, [
            new ParameterValuePropertyPair('labelPositioning', 'text-align'),
            new ParameterValuePropertyPair('labelColor', 'color'),
            new ParameterValuePropertyPair('labelFontSize', 'font-size')]
        );

        return {
            active: this.active,
            hint: this.getParameterValue('hint'),
            label: this.getParameterValue('label'),
            currentLength: 0,
            maxLength: this.getParameterValue('maxLength'),
            maxLengthVisible: this.getParameterValue('maxLengthVisible'),
            textareaStyle: textareaStyle,
            labelStyle: labelStyle,
            clearInput: this.getParameterValue('clearInput'),
            mandatory: this.getParameterValue('mandatory'),
            mandatoryReminder: this.getParameterValue('mandatoryReminder'),
            validateOnSkip: this.getParameterValue('validateOnSkip')
        }
    }
}