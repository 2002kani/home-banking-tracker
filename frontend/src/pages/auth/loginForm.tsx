import { useState } from "react";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { z } from "zod";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Button } from "@/components/ui/button";
import { useAuth } from "@/context/authContext";

const loginSchema = z.object({
  email: z.email("Keine gültige E-Mail"),
  password: z.string().min(8, "Mindestens 8 Zeichen"),
});

type LoginFormValues = z.infer<typeof loginSchema>;

export function LoginForm() {
  const { login } = useAuth();
  const {
    register,
    handleSubmit,
    formState: { errors, isSubmitting },
  } = useForm<LoginFormValues>({ resolver: zodResolver(loginSchema) });

  const [serverError, setServerError] = useState<string | null>(null);

  const onSubmit = async (data: LoginFormValues) => {
    try {
      await login({ email: data.email, password: data.password });
    } catch {
      setServerError("E-Mail oder Passwort falsch.");
    }
  };

  return (
    <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
      <div className="space-y-2">
        <Label htmlFor="login-email">E-Mail</Label>
        <Input
          id="login-email"
          type="email"
          placeholder="name@beispiel.de"
          autoComplete="email"
          {...register("email")}
        />
        {errors.email && (
          <p className="text-[12px] text-destructive">{errors.email.message}</p>
        )}
      </div>
      <div className="space-y-2">
        <div className="flex items-center justify-between">
          <Label htmlFor="login-password">Passwort</Label>
          <button
            type="button"
            className="text-[11px] text-muted-foreground hover:text-foreground transition-colors"
          >
            Vergessen?
          </button>
        </div>
        <Input
          id="login-password"
          type="password"
          autoComplete="current-password"
          {...register("password")}
        />
        {errors.password && (
          <p className="text-[12px] text-destructive">
            {errors.password.message}
          </p>
        )}
      </div>
      {serverError && (
        <p className="text-[12px] text-destructive">{serverError}</p>
      )}
      <Button type="submit" className="w-full mt-2" disabled={isSubmitting}>
        {isSubmitting ? "Wird angemeldet..." : "Anmelden"}
      </Button>
    </form>
  );
}
