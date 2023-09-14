'use client';
import { useState } from "react";

export default function RootLayout({
  children,
}: {
  children: React.ReactNode
}) {
  const [count, setCount] = useState(0);

  return (
    <html>
      <head />
        <body>
          <h1>Click Button for count up.</h1>
          <button onClick={() => setCount(count + 1)}>+</button>
          <div>{count}</div>
          {children}
      </body>
    </html>
  );
  // return (
  //   <html lang="en">
  //     <body>{children}</body>
  //   </html>
  // )
}
