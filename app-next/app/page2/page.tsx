import Link from "next/link";
import Pages from "../../pages";

export default function Page() {

  return (
    <>
      <h1>page2</h1>
      <Link href={'page1'}>Page1へ</Link>
      {/*<Pages/>*/}
    </>
  )
}
