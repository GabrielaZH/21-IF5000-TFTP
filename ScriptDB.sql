CREATE DATABASE IF5000_TFTP_21

USE IF5000_TFTP_21


/****** Object:  Table [dbo].[Client]    Script Date: 19/5/2021 13:02:49 ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dbo].[Client](
	[Client_Id] [int] IDENTITY(1,1) NOT NULL,
	[Name] [nvarchar](10) NOT NULL,
	[Password] varbinary(256) NOT NULL,
 CONSTRAINT [PK_Table_1] PRIMARY KEY CLUSTERED 
(
	[Client_Id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO



CREATE  PROCEDURE SaveClient (@Name nvarchar(10), @Password nvarchar(8)) WITH ENCRYPTION, RECOMPILE
AS BEGIN
	  BEGIN TRY
			BEGIN TRANSACTION SaveClient
			INSERT [Client] ([Name],[Password])
			VALUES (@Name, ENCRYPTBYPASSPHRASE('password', @Password))
			COMMIT TRANSACTION SaveClient

	  END TRY

	 BEGIN CATCH
		    DECLARE @ErrorMessage nvarchar(4000),  @ErrorSeverity int;
            SELECT @ErrorMessage = ERROR_MESSAGE(),@ErrorSeverity = ERROR_SEVERITY();
            RAISERROR(@ErrorMessage, @ErrorSeverity, 1);
		    ROLLBACK TRANSACTION SaveClient
	END  CATCH

END


CREATE Procedure Login (@Name nvarchar(10), @Password nvarchar(8)) WITH ENCRYPTION, RECOMPILE
AS BEGIN
    BEGIN TRY	
		BEGIN TRANSACTION Login
		Declare @PassEncode As nvarchar(300)
		Declare @PassDecode As nvarchar(50)
	
		SELECT @PassEncode = [Password] FROM [Client] WHERE [Name] = @Name
		SET @PassDecode = DECRYPTBYPASSPHRASE('password', @PassEncode)

		SELECT [Client_Id],[Name],[Password]= @PassDecode FROM [Client] WHERE @Password=@PassDecode AND [Name] = @Name

	    COMMIT TRANSACTION Login
	  END TRY

	 BEGIN CATCH
		    DECLARE @ErrorMessage nvarchar(4000),  @ErrorSeverity int;
            SELECT @ErrorMessage = ERROR_MESSAGE(),@ErrorSeverity = ERROR_SEVERITY();
            RAISERROR(@ErrorMessage, @ErrorSeverity, 1);
		    ROLLBACK TRANSACTION Login
	END  CATCH
 
END

