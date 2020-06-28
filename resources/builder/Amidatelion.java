package Unit;
import com.materiabot.GameElements.Ability;
import com.materiabot.GameElements.Ability.Type;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Amidatelion extends _Default{
	public Amidatelion() {
		super("Amidatelion", "ami", "ami", "ami", "ami", "ami", "ami", "ami", "ami", "ami", "ami", "ami", "ami", "ami", "ami", "ami", "ami");
	}
	
	@Override
	public List<Ability> getAbility(Type type) {
		int[] ids = new int[0];
		switch(type) {
			case BRV:
				ids = new int[]{8819};
				break;
			case HP:
				ids = new int[]{8820};
				break;
			case S1:
				ids = new int[]{8483};
				break;
			case S2:
				ids = new int[]{8487};
				break;
			case EX:
				ids = new int[]{8818};
				break;
			case AA:
				ids = new int[]{8492};
				break;
			case LD:
				ids = new int[]{8917};
				break;
			case BT:
				ids = new int[]{};
				break;
			case CA:
				ids = new int[]{};
				break;
		}
		if(ids.length == 0)
			return super.getAbility(type);
		else
			return IntStream.of(ids).boxed().map(i -> this.getSpecificAbility(i)).collect(Collectors.toList());
	}
}