import {AbstractControl, ValidatorFn} from '@angular/forms';

export class CustomeDateValidators {
    static fromToDate(startDateField: string, endDateField: string, errorName: string = 'fromToDate'): ValidatorFn {
        return (formGroup: AbstractControl): { [key: string]: boolean } | null => {
            const startDate = formGroup.get(startDateField).value;
            const endDate = formGroup.get(endDateField).value;
            // Ausing the fromDate and toDate are numbers. In not convert them first after null check
            if ((startDate !== null && endDate !== null) && startDate > endDate) {
                return {[errorName]: true};
            }
            return null;
        };
    }
}
