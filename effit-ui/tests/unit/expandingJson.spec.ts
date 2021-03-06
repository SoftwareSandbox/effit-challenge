// import { shallowMount } from '@vue/test-utils';

describe('expanding incoming json data', () => {
    it('adding a selectable property', () => {
        const data: any[] = [{name: 'Snarf'}, {name: 'Lion-O'}];

        const expandedData = data.map((challengeFromData) => {
            return {...challengeFromData, ...{selected: false}};
        });

        expect(expandedData).toEqual([{name: 'Snarf', selected: false}, {name: 'Lion-O', selected: false}]);
    });
});
