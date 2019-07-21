export interface Competitor {
    id: string;
    name: string;
    totalScore: number;
}

export interface Competition {
    name: string;
    startDate: string;
    endDate: string;
    competitors: Competitor[];
    started: boolean;
}
