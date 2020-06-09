export interface IEmployee {
  id?: number;
  firstName?: string;
  lastName?: string;
  salary?: number;
}

export class Employee implements IEmployee {
  constructor(public id?: number, public firstName?: string, public lastName?: string, public salary?: number) {}
}
