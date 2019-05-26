export interface Competitor {
    id: string;
    name: string;
}

export interface Competition {
    name: string;
    startDate: string;
    endDate: string;
    competitors: Competitor[];
}
