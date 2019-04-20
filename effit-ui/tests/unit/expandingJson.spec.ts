// import { shallowMount } from '@vue/test-utils';

describe('expanding incoming json data', () => {
    it('adding a selectable property', () => {
        type SelectableData = {name:string, selected:boolean};

        let data: any[] = [{name:"Snarf"}, {name:"Lion-O"}];

        let expandedData = data.map((challengeFromData) => {
            return {...challengeFromData, ...{selected: false}};
        });

        expect(expandedData).toEqual([{name: "Snarf", selected: false}, {name: "Lion-O", selected: false}]);
    });
});
