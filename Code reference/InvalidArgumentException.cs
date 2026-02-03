using System;

public class InvalidArgumentException : Exception
{
	public InvalidArgumentException(string errorMessage) : base(errorMessage) { }

	public InvalidArgumentException() : base() { }
}
