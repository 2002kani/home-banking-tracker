export interface Transaction {
  id: string;
  title: string;
  amount: number;
  category: string;
  date: string;
  time?: string;
  type: "income" | "expense";
  merchant?: string;
}

export interface Category {
  id: string;
  name: string;
  color: string;
  icon: string;
  budget?: number;
  spent?: number;
}
