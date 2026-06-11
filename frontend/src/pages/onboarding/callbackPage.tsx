import { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { getAccounts } from "@/api/generated/account-service";
import { Loader2 } from "lucide-react";

export default function CallbackPage() {
  const navigate = useNavigate();

  useEffect(() => {
    let attempts = 0;
    const MAX_ATTEMPTS = 15;

    const poll = async () => {
      try {
        const res = await getAccounts();
        if ((res.data?.length ?? 0) > 0) {
          navigate("/dashboard", { replace: true });
          return;
        }
      } catch {
        // ignore, retry
      }

      attempts++;
      if (attempts >= MAX_ATTEMPTS) {
        navigate("/onboarding/bank-select", { replace: true });
        return;
      }

      setTimeout(poll, 2000);
    };

    poll();
  }, [navigate]);

  return (
    <div className="flex flex-col items-center justify-center gap-4 text-center">
      <Loader2 className="h-8 w-8 animate-spin text-muted-foreground" />
      <p className="text-sm text-muted-foreground">
        Bankverbindung wird hergestellt…
      </p>
    </div>
  );
}
