export interface UserDto {
  id: number;
  email: string;
  displayName: string;
  createdAt: string;
  updatedAt?: string | null;
}

export interface UserCreateRequest {
  email: string;
  displayName: string;
  password: string;
}

export interface LoginRequest {
  email: string;
  password: string;
}

export interface LoginResponse {
  tokenType: string;
  accessToken: string;
  expiresIn: number;
}

export interface RegisterResponse {
  user: UserDto;
  token?: string;
  tokenType?: string;
  expiresIn?: number;
}
