export function decodeJwtPayload(token: string) {
  return JSON.parse(atob(token.split(".")[1]));
}
