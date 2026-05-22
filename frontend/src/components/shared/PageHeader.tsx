import type { ReactNode } from "react";

interface IProps {
  title: string;
  description?: string;
  actions?: ReactNode;
}

function PageHeader({ title, description, actions }: IProps) {
  return (
    <div className="-mx-6 -mt-6 border-b border-border px-6 py-5">
      <div className="flex items-center justify-between gap-4">
        <div>
          <h1 className="text-2xl font-semibold tracking-tight">{title}</h1>
          {description && (
            <p className="mt-1 text-sm text-muted-foreground">{description}</p>
          )}
        </div>
        {actions && <div className="flex items-center gap-2">{actions}</div>}
      </div>
    </div>
  );
}

export default PageHeader;
