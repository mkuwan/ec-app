import Link from "next/link";

export default function Page () {
  return (
    <>
      <h1>page1</h1>
      <Link href={'page2'}>Page2へ</Link>
    </>
  )
}