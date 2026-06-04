import { useState } from "react";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { z } from "zod";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Button } from "@/components/ui/button";
import { useAuth } from "@/context/authContext";

const registerSchema = z
  .object({
    firstName: z.string().min(1, "Pflichtfeld"),
    lastName: z.string().min(1, "Pflichtfeld"),
    email: z.email("Keine gültige E-Mail"),
    password: z.string().min(8, "Mindestens 8 Zeichen"),
    confirmPassword: z.string(),
  })
  .refine((data) => data.password === data.confirmPassword, {
    message: "Passwörter stimmen nicht überein",
    path: ["confirmPassword"],
  });

type RegisterFormValues = z.infer<typeof registerSchema>;

export function RegisterForm() {
  const { register: registerUser } = useAuth();
  const [serverError, setServerError] = useState<string | null>(null);
  const {
    register,
    handleSubmit,
    formState: { errors, isSubmitting },
  } = useForm<RegisterFormValues>({ resolver: zodResolver(registerSchema) });

  const onSubmit = async (data: RegisterFormValues) => {
    try {
      await registerUser({
        firstName: data.firstName,
        lastName: data.lastName,
        email: data.email,
        password: data.password,
      });
    } catch {
      setServerError("Registrierung fehlgeschlagen. E-Mail bereits vergeben?");
    }
  };

  return (
    <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
      <div className="grid grid-cols-2 gap-3">
        <div className="space-y-2">
          <Label htmlFor="register-firstname">Vorname</Label>
          <Input
            id="register-firstname"
            placeholder="Max"
            autoComplete="given-name"
            {...register("firstName")}
          />
          {errors.firstName && (
            <p className="text-[12px] text-destructive">
              {errors.firstName.message}
            </p>
          )}
        </div>
        <div className="space-y-2">
          <Label htmlFor="register-lastname">Nachname</Label>
          <Input
            id="register-lastname"
            placeholder="Mustermann"
            autoComplete="family-name"
            {...register("lastName")}
          />
          {errors.lastName && (
            <p className="text-[12px] text-destructive">
              {errors.lastName.message}
            </p>
          )}
        </div>
      </div>
      <div className="space-y-2">
        <Label htmlFor="register-email">E-Mail</Label>
        <Input
          id="register-email"
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
        <Label htmlFor="register-password">Passwort</Label>
        <Input
          id="register-password"
          type="password"
          autoComplete="new-password"
          {...register("password")}
        />
        {errors.password && (
          <p className="text-[12px] text-destructive">
            {errors.password.message}
          </p>
        )}
      </div>
      <div className="space-y-2">
        <Label htmlFor="register-confirm">Passwort bestätigen</Label>
        <Input
          id="register-confirm"
          type="password"
          autoComplete="new-password"
          {...register("confirmPassword")}
          className={
            errors.confirmPassword
              ? "border-destructive focus-visible:ring-destructive"
              : ""
          }
        />
        {errors.confirmPassword && (
          <p className="text-[12px] text-destructive">
            {errors.confirmPassword.message}
          </p>
        )}
      </div>
      {serverError && (
        <p className="text-[12px] text-destructive">{serverError}</p>
      )}
      <Button type="submit" className="w-full mt-2" disabled={isSubmitting}>
        {isSubmitting ? "Wird registriert..." : "Konto erstellen"}
      </Button>
    </form>
  );
}
