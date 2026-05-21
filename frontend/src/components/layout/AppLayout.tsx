import { Link, Outlet, useLocation } from "react-router-dom";

import { Avatar, AvatarFallback } from "../ui/avatar";
import {
  Sidebar,
  SidebarContent,
  SidebarFooter,
  SidebarGroup,
  SidebarGroupContent,
  SidebarGroupLabel,
  SidebarHeader,
  SidebarInset,
  SidebarMenu,
  SidebarMenuButton,
  SidebarMenuItem,
  SidebarProvider,
} from "../ui/sidebar";
import { ChevronsUpDown } from "lucide-react";
import { sidebarNav, sidebarSecondaryNav, version } from "@/lib/constants";
import Header from "./Header";

export default function AppLayout() {
  const location = useLocation();

  return (
    <SidebarProvider>
      <Sidebar collapsible="icon">
        <SidebarHeader className="border-b border-sidebar-border">
          <div className="flex items-center gap-2 px-2 py-1.5">
            <div className="flex h-7 w-7 items-center justify-center rounded-md bg-sidebar-primary text-sidebar-primary-foreground">
              <span className="font-mono text-xs font-bold">H</span>
            </div>
            <div className="flex flex-col leading-tight group-data-[collapsible=icon]:hidden">
              <span className="text-sm font-semibold tracking-tight">
                Home Banking
              </span>
              <span className="text-[10px] text-muted-foreground font-mono">
                {version}
              </span>
            </div>
          </div>
        </SidebarHeader>

        <SidebarContent>
          <SidebarGroup>
            <SidebarGroupLabel className="text-[11px] uppercase tracking-wider">
              Übersicht
            </SidebarGroupLabel>
            <SidebarGroupContent>
              <SidebarMenu>
                {sidebarNav.map((item) => (
                  <SidebarMenuItem key={item.url} className="pb-1">
                    <SidebarMenuButton
                      render={<Link to={item.url} />}
                      isActive={location.pathname === item.url}
                      tooltip={item.title}
                    >
                      <item.icon className="h-4 w-4" />
                      <span>{item.title}</span>
                    </SidebarMenuButton>
                  </SidebarMenuItem>
                ))}
              </SidebarMenu>
            </SidebarGroupContent>
          </SidebarGroup>

          <SidebarGroup>
            <SidebarGroupLabel className="text-[11px] uppercase tracking-wider">
              Verwaltung
            </SidebarGroupLabel>
            <SidebarGroupContent>
              <SidebarMenu>
                {sidebarSecondaryNav.map((item) => (
                  <SidebarMenuItem key={item.url} className="pb-1">
                    <SidebarMenuButton
                      render={<Link to={item.url} />}
                      isActive={location.pathname === item.url}
                      tooltip={item.title}
                    >
                      <item.icon className="h-4 w-4" />
                      <span>{item.title}</span>
                    </SidebarMenuButton>
                  </SidebarMenuItem>
                ))}
              </SidebarMenu>
            </SidebarGroupContent>
          </SidebarGroup>
        </SidebarContent>

        <SidebarFooter className="border-t border-sidebar-border">
          <SidebarMenu>
            <SidebarMenuItem>
              <SidebarMenuButton
                size="lg"
                className="data-[state=open]:bg-sidebar-accent"
              >
                <Avatar className="h-7 w-7 rounded-md">
                  <AvatarFallback className="rounded-md bg-sidebar-primary text-sidebar-primary-foreground text-xs">
                    KK
                  </AvatarFallback>
                </Avatar>
                <div className="flex flex-1 flex-col text-left text-xs leading-tight">
                  <span className="font-medium">Kani Karadag</span>
                  <span className="text-muted-foreground">test@email.com</span>
                </div>
                <ChevronsUpDown className="ml-auto h-4 w-4 opacity-50" />
              </SidebarMenuButton>
            </SidebarMenuItem>
          </SidebarMenu>
        </SidebarFooter>
      </Sidebar>

      <SidebarInset>
        <Header title="Dashboard"></Header>
        <main className="p-6">
          <Outlet />
        </main>
      </SidebarInset>
    </SidebarProvider>
  );
}
