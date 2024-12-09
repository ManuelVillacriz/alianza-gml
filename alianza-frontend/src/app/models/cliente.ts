import { Gender } from "./gender";
import { Generic } from "./generic";

export class Cliente implements Generic {
    id?: number;
    name: string;
    lastName: string;
    gender?: Gender;
    sharedKey?: string;
    phone?: string;
    email: string;
    startDate?: Date;
	endDate?: Date;
    dataAdded?: Date;
}

